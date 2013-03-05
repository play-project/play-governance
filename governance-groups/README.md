# Group Services

Groups are stored in the metadata service as JSON/RDF.

## Example

    "http://groups.event-processing.org/ids/MyGroupName#group" :
    {
      "http://purl.org/dc/elements/1.1/title": [ { "type" : "literal", "value" : "My Group title" } ],
      "http://purl.org/dc/elements/1.1/description": [ { "type" : "literal", "value" : "A group of Facebook Wall updates." } ],
      "http://www.w3.org/2002/06/xhtml2/icon": [ { "type" : "uri", "value" : "http://s-static.ak.facebook.com/rsrc.php/yi/r/q9U99v3_saj.ico" } ]
    }

@chamerling
  